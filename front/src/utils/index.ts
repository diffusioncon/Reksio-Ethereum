import { DateTime } from 'luxon';
import { useEffect } from 'react';
import { Status, FileInfo } from '../types/fileInfo';

export function isoToDate(isoDate: string): string {
  const date = DateTime.fromISO(isoDate);
  return date.toLocaleString(DateTime.DATE_FULL);
}

export function isoToDateTime(isoDate: string): string {
  const date = DateTime.fromISO(isoDate);
  return date.toLocaleString(DateTime.DATETIME_FULL);
}

export function useEnterKey(callback: () => void) {
  useEffect(() => {
    const listener = (e: KeyboardEvent) => {
      if (e.key === 'Enter') {
        callback();
      }
    };
    window.addEventListener('keydown', listener);
    return () => window.removeEventListener('keydown', listener);
  });
}

export function getFileStatus(file: FileInfo): Status {
  if (!file.transactionHash) {
    return Status.PENDING;
  }
  if (file.hashIsOk) {
    return Status.VALID;
  }
  return Status.INVALID;
}
