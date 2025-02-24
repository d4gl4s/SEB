import {Component, inject, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { ExchangeRateService } from '../service/exchange-rate.service';
import { ExchangeRate } from '../model/exchange-rate.model';
import {RouterLink, RouterLinkActive} from '@angular/router';

@Component({
  selector: 'app-exchange-rates',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: 'exchange-rates.component.html',
  styleUrls: ['exchange-rate.component.css']
})
export class ExchangeRatesComponent implements OnInit {
  exchangeRates: ExchangeRate[] = [];
  loading = false;
  error: string | null = null;

  private exchangeRateService = inject(ExchangeRateService);


  ngOnInit(): void {
    console.log("init")
    this.fetchExchangeRates();
  }

  fetchExchangeRates(): void {
    this.loading = true;
    this.error = null;

    this.exchangeRateService.getLatestRates()
      .subscribe({
        next: (data: ExchangeRate[]) => {
          this.exchangeRates = data;
          this.loading = false;
        },
        error: (error) => {
          this.error = 'Failed to fetch exchange rates. Please try again later.';
          this.loading = false;
          console.error('Error fetching exchange rates:', error);
        }
      });
  }
}
