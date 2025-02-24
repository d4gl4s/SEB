export interface ExchangeRateHistory {
  currencyCode: string;
  currencyName: string;
  flag: string;
  symbol: string;
  entries: ExchangeRateHistoryEntry[]
}

interface ExchangeRateHistoryEntry {
  date: string;
  rate: number;
}
