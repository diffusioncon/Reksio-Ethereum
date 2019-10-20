import { FileInfo, FileStatus } from '../../types/fileInfo';

function createData(
  filename: string,
  hash: string,
  uploadDateTime: string,
  status: FileStatus,
  isRefreshing: boolean = false
): FileInfo {
  return {
    filename,
    hash,
    uploadDateTime,
    status,
    isRefreshing,
  };
}

export const FILES: FileInfo[] = [
  createData(
    'file1.mov',
    '0xdf4d9c45e42bf92451f64044b4c94d9879791c6b52c0d108facab6518ae36136',
    '2019-02-02T11:15:24+0000',
    'valid'
  ),
  createData(
    'file2.mov',
    '0xb0d6219d57477a5cfcaf5162ccbb6c3b2ac3d00d64d5f55c43b3f895a4659d1a',
    '2019-04-30T15:30:11+0000',
    'valid'
  ),
  createData(
    'file4.mov',
    '0x4f44d988446a468ff8fd5e4a96c520b539f921e01d0ae97378cf5e9d0cd28e71',
    '2019-08-19T09:57:21+0000',
    'invalid'
  ),
  createData(
    'file3.mov',
    '0x319ed45ab1b9a6125c768341c228864f6fc38fd4c402bc57f71fe33ddb4c529f',
    '2019-06-08T08:08:31+0000',
    'valid'
  ),
  createData(
    'file5.mov',
    '0x38f6410505d9f7580bddf4b500aa5963fd651d344eab8476e0068519741cef81',
    '2019-10-22T02:11:51+0000',
    'valid'
  ),
  createData(
    'file5.mov',
    '0x38f6410505d9f7580bddf4b500aa5963fd651d344eab8476e0068519741cef81',
    '2019-10-22T02:11:51+0000',
    'valid'
  ),
  createData(
    'file5.mov',
    '0x38f6410505d9f7580bddf4b500aa5963fd651d344eab8476e0068519741cef81',
    '2019-10-22T02:11:51+0000',
    'valid'
  ),
  createData(
    'file5.mov',
    '0x38f6410505d9f7580bddf4b500aa5963fd651d344eab8476e0068519741cef81',
    '2019-10-22T02:11:51+0000',
    'valid'
  ),
  createData(
    'file5.mov',
    '0x38f6410505d9f7580bddf4b500aa5963fd651d344eab8476e0068519741cef81',
    '2019-10-22T02:11:51+0000',
    'valid'
  ),
  createData(
    'file5.mov',
    '0x38f6410505d9f7580bddf4b500aa5963fd651d344eab8476e0068519741cef81',
    '2019-10-22T02:11:51+0000',
    'valid'
  ),
  createData(
    'file5.mov',
    '0x38f6410505d9f7580bddf4b500aa5963fd651d344eab8476e0068519741cef81',
    '2019-10-22T02:11:51+0000',
    'valid'
  ),
  createData(
    'file5.mov',
    '0x38f6410505d9f7580bddf4b500aa5963fd651d344eab8476e0068519741cef81',
    '2019-10-22T02:11:51+0000',
    'valid'
  ),
  createData(
    'file5.mov',
    '0x38f6410505d9f7580bddf4b500aa5963fd651d344eab8476e0068519741cef81',
    '2019-10-22T02:11:51+0000',
    'valid'
  ),
];
