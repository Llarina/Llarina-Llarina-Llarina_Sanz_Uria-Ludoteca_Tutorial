export interface LoanSearchParams {
  gameName?: string;
  clientName?: string;
  loanDate?: Date;
  pageNumber?: number;
  pageSize?: number;
  sort?: string; // e.g., 'id,asc'
}