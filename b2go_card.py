# -*- coding: utf-8 -*-

import blocksec2go
import string
import time

import logging
logger = logging.getLogger()

from smartcard.System import readers


READER_ID = "Identiv Identiv uTrust 4701 F Dual Interface Reader [uTrust 4701 F CL Reader] (55041727202248) 01 00"
# READER_ID = "Identiv Identiv uTrust 4701 F Dual Interface Reader"

class Blocksec2goWrapper:
    def __init__(self):
        self.reader = None
        self.key_id = None

    def init(self, key_id=1):
        self.key_id = key_id
        while self.reader == None:
            try:
                self.reader = blocksec2go.find_reader(READER_ID)
            except Exception as details:
                logger.debug(details)
                if "No reader found" != str(details) and "No card on reader" != str(details):
                    logger.error("ERROR:", details)
                    raise Exception(f"Reader error: {details}")
                time.sleep(1)
        try:
            blocksec2go.select_app(self.reader)
        except Exception as details:
            logger.error("ERROR:", details)
            raise SystemExit

    def get_pub_key(self):
        _, _, pub_key = blocksec2go.get_key_info(self.reader, self.key_id)
        # remove 0x04 byte
        unprefixed_pub_key = pub_key[1:]
        return unprefixed_pub_key

    def generate_signature_der(self, data):
        _, _, signature = blocksec2go.generate_signature(
            self.reader, self.key_id, data
        )
        return signature

def init_blocksec2go_card():
    card = Blocksec2goWrapper()
    card.init(key_id=1)
    return card
