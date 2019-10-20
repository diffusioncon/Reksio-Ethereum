export interface Pageable<T> {
  content: T[];
  totalPages: number;
  number: number;
}
