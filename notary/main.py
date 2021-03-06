#!/usr/bin/env python3
# -*- coding: utf-8 -*-

#———————————————————————————————————————————————————————————————————————————
# Logging configuration

import logging
logging.basicConfig(format="%(levelname)s:%(asctime)s:%(name)s:%(message)s")
logger = logging.getLogger()
logger.setLevel(logging.DEBUG)

#———————————————————————————————————————————————————————————————————————————
# General Imports

import ecdsa
import json

# Settings are kept in .env
from dotenv import load_dotenv
load_dotenv()

#———————————————————————————————————————————————————————————————————————————
# Flask app configuration

from flask import Flask, jsonify, request

app = Flask(__name__)

#———————————————————————————————————————————————————————————————————————————
# Ethereum configuration

import smartcard.Exceptions
import blocksec2go.comm.base

from eth_account._utils.transactions import (
    serializable_unsigned_transaction_from_dict,
    encode_transaction,
)
from web3 import Web3, HTTPProvider

# Internal utilities
from b2go_card import init_blocksec2go_card
import web3_utils

# kovan testnet
CHAIN_ID = 42

#———————————————————————————————————————————————————————————————————————————

def load_file_notary_contract():
    with open('deployed/fileNotary.json') as json_file:
        contract = json.load(json_file)
    notary = w3.eth.contract(
        address=contract['address'],
        abi=contract['abi']
    )
    return notary

def get_own_hash_ethereum(file_id):
    #TODO: check for card status
    hash = notary.functions.getFileHash(card_address, file_id).call({
        'from': card_address
    })
    return hash.hex()

def get_hash_ethereum(address, file_id):
    #TODO: check for card status
    hash = notary.functions.getFileHash(address, file_id).call({
        'from': address
    })
    return hash.hex()

def save_hash_ethereum(file_id, file_hash):

    # Prepare transaction data
    raw_unsigned_transaction = notary.functions.setFileHash(file_id, file_hash).buildTransaction()

    nonce = w3.eth.getTransactionCount(card_address)
    logger.debug(f"Got nonce: {nonce}")

    raw_unsigned_transaction['nonce'] = nonce
    raw_unsigned_transaction['to'] = notary.address

    # Serialize the transaction
    unsigned_transaction = serializable_unsigned_transaction_from_dict(
        raw_unsigned_transaction
    )
    unsigned_transaction_hash = unsigned_transaction.hash()
    logger.debug(f"unsigned transaction hash: {unsigned_transaction_hash.hex()}")

    # Sign the transaction
    signature = card.generate_signature_der(unsigned_transaction_hash)
    logger.debug(f"signature: {signature.hex()}")

    v = web3_utils.get_v(signature, unsigned_transaction_hash, public_key, CHAIN_ID)
    r, s = web3_utils.sigdecode_der(signature)

    # logger.debug(f"r: {r}\ns: {s}")
    # logger.debug(f"v: {v}")
    encoded_transaction = encode_transaction(unsigned_transaction, vrs=(v, r, s))
    tx_hash = w3.eth.sendRawTransaction(encoded_transaction)
    logger.debug(f"tx hash: {tx_hash.hex()}")

    return tx_hash.hex()

#———————————————————————————————————————————————————————————————————————————
# Endpoints

@app.route("/api/v1/hash/<file_id>", methods = ['GET'])
def get_own_hash(file_id):
    logger.debug(f"get_own_hash({file_id})")
    hash = get_own_hash_ethereum(file_id)
    return jsonify({
        "result": "OK",
        "hash": "0x" + hash
    })

@app.route("/api/v1/hash/<file_owner>/<file_id>", methods = ['GET'])
def get_hash(file_owner, file_id):
    logger.debug(f"get_hash({file_owner}, {file_id})")
    hash = get_hash_ethereum(file_owner, file_id)
    return jsonify({
        "result": "OK",
        "hash": "0x" + hash
    })

@app.route("/api/v1/hash", methods = ['POST'])
def save_hash():
    global card
    request_data = request.get_json()
    file_id = request_data['file_id']
    file_hash = request_data['file_hash']
    logger.debug(f"save_hash({file_id}, {file_hash})")
    try:
        tx_hash = save_hash_ethereum(file_id, file_hash)
    except (smartcard.Exceptions.CardConnectionException, blocksec2go.comm.base.CardError, AttributeError) as e:
        logger.warning(e)
        try:
            # Reinitialize and retry
            card = init_blocksec2go_card(retrying=True)
            if card is None:
                return jsonify({
                    "result": "Could not initialize card"
                })
            tx_hash = save_hash_ethereum(file_id, file_hash)
        except (smartcard.Exceptions.CardConnectionException, blocksec2go.comm.base.CardError, AttributeError) as e:
            logger.error(e)
            return jsonify({
                "result": "Card is gone"
            })
    return jsonify({
        "result": "OK",
        "tx_hash": tx_hash
    })

#———————————————————————————————————————————————————————————————————————————
# Main

if __name__ == "__main__":
    card = init_blocksec2go_card()
    w3, public_key, card_address = web3_utils.init_web3(card)
    logger.info(f"Using hardware wallet with address {card_address}")
    notary = load_file_notary_contract()
    app.run(host='0.0.0.0', port='8880')

#———————————————————————————————————————————————————————————————————————————
