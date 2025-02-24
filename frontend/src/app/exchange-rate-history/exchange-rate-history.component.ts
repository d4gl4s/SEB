import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ExchangeRateService } from '../service/exchange-rate.service';
import { ActivatedRoute } from '@angular/router';
import { ExchangeRateHistory } from '../model/exchange-rate-history.model';
import { BaseChartDirective } from 'ng2-charts';
import { ChartOptions } from 'chart.js';

@Component({
  selector: 'exchange-rate-history',
  standalone: true,
  imports: [CommonModule, BaseChartDirective],
  templateUrl: 'exchange-rate-history.component.html',
  styleUrls: ['exchange-rate-history.component.css']
})
export class ExchangeRateHistoryComponent implements OnInit {
  public lineChartData: any = {
    labels: [],
    datasets: [
      {
        data: [],
        label: '',
        fill: true,
        borderColor: 'blue',
        backgroundColor: 'rgba(0,0,255,0.1)'
      }
    ]
  };
  public lineChartOptions: ChartOptions<'line'> = {responsive: true};
  public lineChartLegend = false;

  private readonly exchangeRateService = inject(ExchangeRateService);
  private readonly route = inject(ActivatedRoute);
  protected exchangeRateHistory: ExchangeRateHistory | null = null;
  loading: boolean = false;
  error: string | null = null;

  ngOnInit(): void {
    const currencyCode = this.route.snapshot.paramMap.get('currencyCode');
    if (currencyCode) this.fetchExchangeRateHistory(currencyCode);
  }

  private fetchExchangeRateHistory(currencyCode: string): void {
    this.loading = true;
    this.error = null;

    this.exchangeRateService.getExchangeRateHistory(currencyCode).subscribe({
      next: (data: ExchangeRateHistory) => {
        this.exchangeRateHistory = data;
        this.loading = false;
        this.updateChartData(90); // Default to 3 months
      },
      error: (error) => {
        this.error = 'Failed to fetch exchange rate history. Please try again later.';
        this.loading = false;
        console.error('Error fetching exchange rates:', error);
      }
    });
  }

  updateChartData(days: number): void {
    if (!this.exchangeRateHistory) return;

    const cutoffDate = new Date();
    cutoffDate.setDate(cutoffDate.getDate() - days);

    const filteredEntries = this.exchangeRateHistory.entries.filter(entry =>
      new Date(entry.date) >= cutoffDate
    );

    const labels = filteredEntries.map(entry => entry.date);
    const rates = filteredEntries.map(entry => entry.rate);

    this.lineChartData = {
      labels,
      datasets: [
        {
          data: rates,
          fill: true,
          borderColor: 'blue',
          backgroundColor: 'rgba(0,0,255,0.1)'
        }
      ]
    };
  }
}
