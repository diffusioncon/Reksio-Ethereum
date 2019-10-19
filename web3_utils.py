# -*- coding: utf-8 -*-

import ecdsa
import os

from web3 import Web3, HTTPProvider

import logging
logger = logging.getLogger()


generator = ecdsa.SECP256k1.generator


def sigdecode_der(sig):
    n = generator.order()
    return ecdsa.util.sigdecode_der(sig, n)


def recover_public_keys(sig, hash):
    curve = generator.curve()
    n = generator.order()
    r, s = sigdecode_der(sig)
    e = ecdsa.util.string_to_number(hash)
    x = r

    # Compute the curve point with x as x-coordinate
    alpha = (pow(x, 3, curve.p()) + (curve.a() * x) + curve.b()) % curve.p()
    beta = ecdsa.numbertheory.square_root_mod_prime(alpha, curve.p())
    y = beta if beta % 2 == 0 else curve.p() - beta

    # Compute the public key
    R1 = ecdsa.ellipticcurve.Point(curve, x, y, n)
    Q1 = ecdsa.numbertheory.inverse_mod(r, n) * (s * R1 + (-e % n) * generator)
    Pk1 = ecdsa.ecdsa.Public_key(generator, Q1)

    # And the second solution
    R2 = ecdsa.ellipticcurve.Point(curve, x, -y, n)
    Q2 = ecdsa.numbertheory.inverse_mod(r, n) * (s * R2 + (-e % n) * generator)
    Pk2 = ecdsa.ecdsa.Public_key(generator, Q2)

    return [Pk1, Pk2]


def find_recovery_id(sig, hash, pub_key):
    vk = ecdsa.VerifyingKey.from_string(pub_key, curve=ecdsa.SECP256k1)
    public_keys = recover_public_keys(sig, hash)
    vk_point = vk.pubkey.point
    if public_keys[0].point == vk_point:
        return 0
    elif public_keys[1].point == vk_point:
        return 1
    else:
        return None


def address_from_pub_key(pub_key):
    pk_hash = Web3.keccak(pub_key)
    address_bytes = pk_hash[-20:]
    address = address_bytes.hex()
    return Web3.toChecksumAddress(address)


def get_v(signature, unsigned_transaction_hash, pub_key, chain_id):
    recovery_id = utils.find_recovery_id(signature, unsigned_transaction_hash, pub_key)
    return 35 + recovery_id + (chain_id * 2)

def init_web3(card):

    # Infura is used to access the Ethereum network
    infura_kovan_endpoint = os.getenv("INFURA_KOVAN_ENDPOINT")
    if infura_kovan_endpoint is None:
        raise Exception("Endpoint not defined in the env")

    w3 = Web3(HTTPProvider(infura_kovan_endpoint))
    if not w3.isConnected():
        raise Exception("Web3 did not connect")
    logger.debug("web3 connected")

    public_key = card.get_pub_key()
    logger.debug(f"pubkey: {public_key.hex()}")
    address = address_from_pub_key(public_key)
    logger.debug(f"address: {address}")

    return w3, public_key, address
