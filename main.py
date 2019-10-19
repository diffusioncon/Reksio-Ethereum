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
# FAUCET_ADDRESS = "0x4d6Bb4ed029B33cF25D0810b029bd8B1A6bcAb7B"

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
    hash = notary.functions.getOwnFileHash(file_id).call({
        'from': address
    })
    return str(hash)

def get_hash_ethereum(address, file_id):
    #TODO: check for card status
    hash = notary.functions.getFileHash(address, file_id).call({
        'from': address
    })
    return str(hash)

def save_hash_ethereum(file_id, file_hash):

    # Prepare transaction data
    transaction_data = notary.functions.setFileHash(file_id, file_hash).encodeABI() 

    logger.debug("Getting nonce...")
    nonce = w3.eth.getTransactionCount(address)
    logger.debug(f"Got nonce: {nonce}")

    raw_unsigned_transaction = {
        "from": address,
        "to": notary.address,
        "value": Web3.toWei("0.0001", "ether"),
        "gas": 21000,
        "gasPrice": Web3.toWei("10", "gwei"),
        "nonce": nonce,
        "chainId": CHAIN_ID,
        "data": transaction_data,
    }
    unsigned_transaction = serializable_unsigned_transaction_from_dict(
        raw_unsigned_transaction
    )
    unsigned_transaction_hash = unsigned_transaction.hash()
    logger.debug(f"unsigned transaction hash: {unsigned_transaction_hash.hex()}")

    signature = card.generate_signature_der(unsigned_transaction_hash)
    logger.debug(f"signature: {signature.hex()}")

    v = web3_utils.get_v(signature, unsigned_transaction_hash, public_key, CHAIN_ID)
    r, s = web3_utils.sigdecode_der(signature)

    logger.debug(f"r: {r}\ns: {s}")
    logger.debug(f"v: {v}")
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
        "result": "OK",  #TODO: check blockchain2go status
        "hash": hash
    })

@app.route("/api/v1/hash/<file_owner>/<file_id>", methods = ['GET'])
def get_hash(file_owner, file_id):
    logger.debug(f"get_own_hash({file_owner}, {file_id})")
    hash = get_hash_ethereum(file_owner, file_id)
    return jsonify({
        "result": "OK",  #TODO: check blockchain2go status
        "hash": hash
    })

@app.route("/api/v1/hash", methods = ['POST'])
def save_hash():
    request_data = request.get_json()
    file_id = request_data['file_id']
    file_hash = request_data['file_hash']
    logger.debug(f"save_hash({file_id}, {file_hash})")
    return save_hash_ethereum(file_id, file_hash)

#———————————————————————————————————————————————————————————————————————————
# Main

if __name__ == "__main__":
    logger.info("Initializing Blockchain2Go reader...")
    card = init_blocksec2go_card()

    w3, public_key, address = web3_utils.init_web3(card)

    notary = load_file_notary_contract()

    app.run(host='0.0.0.0', port='8880')

#———————————————————————————————————————————————————————————————————————————
