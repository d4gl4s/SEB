import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ExchangeRate } from '../model/exchange-rate.model';
import { ExchangeRateHistory } from '../model/exchange-rate-history.model';

@Injectable({ providedIn: 'root'})
export class ExchangeRateService {
  private apiUrl = 'http://localhost:8080/api/v1/exchange-rates';

  constructor(private http: HttpClient) { }

  getLatestRates(): Observable<ExchangeRate[]> {
    return this.http.get<ExchangeRate[]>(this.apiUrl);
  }

  getExchangeRateHistory(currency:string): Observable<ExchangeRateHistory>{
    const url: string = this.apiUrl+"/"+currency+"/history";
    return this.http.get<ExchangeRateHistory>(url)
  }
}
