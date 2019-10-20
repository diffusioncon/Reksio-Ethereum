export interface FileInfo {
  filename: string;
  hash: string;
  uploadDateTime: string;
  hashCalculationDateTime: string;
  hashIsOk: boolean;
  isRefreshing: boolean;
  transactionHash: string;
}

export enum Status {
  VALID,
  INVALID,
  PENDING,
}
