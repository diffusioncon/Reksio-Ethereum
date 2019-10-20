import { FileStatus } from '../types/fileInfo';
import { DateTime } from 'luxon';

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
