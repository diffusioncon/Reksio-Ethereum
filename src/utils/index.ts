import { FileStatus } from '../types/fileInfo';
import { DateTime } from 'luxon';
import { useEffect } from 'react';

export function isStatusValid(status: FileStatus) {
  return status === 'valid';
}

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
