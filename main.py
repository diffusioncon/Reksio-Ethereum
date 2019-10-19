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

import os
import ecdsa

# Settings are kept in .env
from dotenv import load_dotenv
load_dotenv()

#———————————————————————————————————————————————————————————————————————————
# Flask app configuration

from flask import Flask

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
FAUCET_ADDRESS = "0x4d6Bb4ed029B33cF25D0810b029bd8B1A6bcAb7B"

logging.info("Initializing Blockchain2Go reader...")
card = init_blocksec2go_card()

w3, public_key, address = init_web3(card)

#———————————————————————————————————————————————————————————————————————————

def init_web3(card):

    # Infura is used to access the Ethereum network
    infura_kovan_endpoint = os.getenv("INFURA_KOVAN_ENDPOINT")
    if infura_kovan_endpoint is None:
        raise Exception("Endpoint not defined in the env")

    w3 = Web3(HTTPProvider(infura_kovan_endpoint))
    if not w3.isConnected():
        raise Exception("Web3 did not connect")
    logging.debug("web3 connected")

    public_key = card.get_pub_key()
    logging.debug(f"pubkey: {public_key.hex()}")
    address = web3_utils.address_from_pub_key(public_key)
    logging.debug(f"address: {address}")

    return w3, public_key, address

def get_hash_ethereum():
    pass


def save_hash_ethereum():
    logging.debug("Getting nonce...")
    nonce = w3.eth.getTransactionCount(address)
    logging.debug(f"Got nonce: {nonce}")

    raw_unsigned_transaction = {
        "to": FAUCET_ADDRESS,
        "value": Web3.toWei("0.0001", "ether"),
        "gas": 21000,
        "gasPrice": Web3.toWei("10", "gwei"),
        "nonce": nonce,
        "chainId": CHAIN_ID,
        # "data": myContract.methods.myMethod(arg, arg2).encodeABI() 
    }
    unsigned_transaction = serializable_unsigned_transaction_from_dict(
        raw_unsigned_transaction
    )
    unsigned_transaction_hash = unsigned_transaction.hash()
    logging.debug(f"unsigned transaction hash: {unsigned_transaction_hash.hex()}")

    signature = card.generate_signature_der(unsigned_transaction_hash)
    logging.debug(f"signature: {signature.hex()}")

    v = web3_utils.get_v(signature, unsigned_transaction_hash, public_key, CHAIN_ID)
    r, s = web3_utils.sigdecode_der(signature)

    logging.debug(f"r: {r}\ns: {s}")
    logging.debug(f"v: {v}")
    encoded_transaction = encode_transaction(unsigned_transaction, vrs=(v, r, s))
    tx_hash = w3.eth.sendRawTransaction(encoded_transaction)
    logging.debug(f"tx hash: {tx_hash.hex()}")

    return tx_hash.hex()

#———————————————————————————————————————————————————————————————————————————
# Endpoints

@app.route("/api/v1/hash/<address>/<file_id>", methods = ['GET'])
def get_hash(address, file_id):
    logging.debug("get_hash()")
    return address + "/" + file_id

@app.route("/api/v1/hash", methods = ['POST'])
def save_hash():
    logging.debug("save_hash()")
    return save_hash_ethereum()

#———————————————————————————————————————————————————————————————————————————
# Main

if __name__ == "__main__":
    app.run()

#———————————————————————————————————————————————————————————————————————————
