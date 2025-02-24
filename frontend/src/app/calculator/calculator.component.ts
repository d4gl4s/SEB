import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ExchangeRateService } from '../service/exchange-rate.service';
import { ExchangeRate } from '../model/exchange-rate.model';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'calculator',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: 'calculator.component.html',
  styleUrls: ['calculator.component.css']
})
export class CalculatorComponent implements OnInit {
  exchangeRates: ExchangeRate[] = [];
  loading = false;
  error: string | null = null;

  amount: number = 1;
  selectedCurrency: string = '';
  convertedAmount: number | null = null;
  conversionRate: number | null = null;
  searchQuery: string = '';
  showDropdown = false;

  private exchangeRateService = inject(ExchangeRateService);

  ngOnInit(): void {
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

  convertCurrency(): void {
    const selectedRate = this.exchangeRates.find(rate => rate.currencyCode === this.selectedCurrency);
    if (selectedRate) {
      this.conversionRate = selectedRate.rate;
      this.convertedAmount = this.amount * selectedRate.rate;
    } else {
      this.conversionRate = null;
      this.convertedAmount = null;
    }
  }

  filteredCurrencies(): ExchangeRate[] {
    return this.exchangeRates.filter(rate =>
      rate.currencyName.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
      rate.currencyCode.toLowerCase().includes(this.searchQuery.toLowerCase())
    );
  }

  filterCurrencies(): void {
    this.exchangeRates = this.exchangeRates.filter(rate =>
      rate.currencyName.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
      rate.currencyCode.toLowerCase().includes(this.searchQuery.toLowerCase())
    );
  }

  selectCurrency(rate: ExchangeRate): void {
    this.selectedCurrency = rate.currencyCode;
    this.searchQuery = `${rate.flag} ${rate.currencyName} (${rate.currencyCode})`;
    this.showDropdown = false;
    this.convertCurrency();
  }
}

