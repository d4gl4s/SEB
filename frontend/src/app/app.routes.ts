import { Routes } from '@angular/router';
import {ExchangeRateHistoryComponent} from './exchange-rate-history/exchange-rate-history.component';
import {ExchangeRatesComponent} from './exchange-rate/exchange-rates.component';
import {CalculatorComponent} from './calculator/calculator.component';

export const routes: Routes = [
  { path: "history/:currencyCode", title: "Exchange Rate History", component: ExchangeRateHistoryComponent },
  { path: "calculator", title: "Currency Calculator", component: CalculatorComponent},
  { path: "", title: "Exchange Rates", component: ExchangeRatesComponent },

];
