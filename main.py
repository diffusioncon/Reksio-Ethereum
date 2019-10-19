#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import ecdsa
import logging

# Ethereum stuff
from eth_account._utils.transactions import (
    serializable_unsigned_transaction_from_dict,
    encode_transaction,
)
from web3 import Web3, HTTPProvider

# Settings are kept in .env
from dotenv import load_dotenv
load_dotenv()

# Internal utilities
from card import Blocksec2goWrapper
import web3_utils

# kovan testnet
CHAIN_ID = 42
FAUCET_ADDRESS = "0x4d6Bb4ed029B33cF25D0810b029bd8B1A6bcAb7B"


def init_blocksec2go_card():
    card = Blocksec2goWrapper()
    card.init(key_id=1)
    return card


def init_web3(card):

    # Infura is used to access the Ethereum network
    infura_kovan_endpoint = os.getenv("INFURA_KOVAN_ENDPOINT")
    if infura_kovan_endpoint is None:
        raise Exception("Endpoint not defined in the env")

    w3 = Web3(HTTPProvider(infura_kovan_endpoint))
    if not w3.isConnected():
        raise Exception("Web3 did not connect")
    print("web3 connected")

    public_key = card.get_pub_key()
    print("pubkey: {}".format(public_key.hex()))
    address = web3_utils.address_from_pub_key(public_key)
    print("address: {}".format(address))

    return w3, public_key, address


def run_transaction(w3, public_key, address):
    print("Getting nonce...")
    nonce = w3.eth.getTransactionCount(address)
    print("Got nonce: {}".format(nonce))

    raw_unsigned_transaction = {
        "to": FAUCET_ADDRESS,
        "value": Web3.toWei("0.0001", "ether"),
        "gas": 21000,
        "gasPrice": Web3.toWei("10", "gwei"),
        "nonce": nonce,
        "chainId": CHAIN_ID,
    }
    unsigned_transaction = serializable_unsigned_transaction_from_dict(
        raw_unsigned_transaction
    )
    unsigned_transaction_hash = unsigned_transaction.hash()
    print("unsigned transaction hash: {}".format(unsigned_transaction_hash.hex()))

    signature = card.generate_signature_der(unsigned_transaction_hash)
    print("signature: {}".format(signature.hex()))

    v = web3_utils.get_v(signature, unsigned_transaction_hash, public_key, CHAIN_ID)
    r, s = web3_utils.sigdecode_der(signature)

    print("r: {}\ns: {}".format(r, s))
    print("v: {}".format(v))
    encoded_transaction = encode_transaction(unsigned_transaction, vrs=(v, r, s))
    tx_hash = w3.eth.sendRawTransaction(encoded_transaction)
    print("tx hash: {}".format(tx_hash.hex()))


def main():
    print("Initializing reader...")
    card = init_blocksec2go_card()
    print("Reader initialized")
    
    w3, public_key, address = init_web3(card)
    run_transaction(w3, public_key, address)


if __name__ == "__main__":
    main()
