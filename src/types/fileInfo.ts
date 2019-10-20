export type FileStatus = 'valid' | 'invalid';

export interface FileInfo {
  filename: string;
  hash: string;
  uploadDateTime: string;
  status: FileStatus;
  isRefreshing: boolean;
}
