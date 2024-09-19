import { Component } from '@angular/core';
import { CocktailService } from '../services/cocktail.service';
import { Cocktail, GuessRequest } from '../../types';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HintsComponent } from './hints/hints.component';
import { ResultComponent } from './result/result.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    InputTextModule,
    ButtonModule,
    FormsModule,
    CommonModule,
    HintsComponent,
    ResultComponent,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent {
  constructor(private cocktailService: CocktailService) {}

  cocktail: Cocktail = {};

  guessRequest: GuessRequest = { guess: '' };

  ngOnInit() {
    this.fetchRandomCocktail();
  }

  fetchRandomCocktail() {
    this.cocktailService
      .getRandomCocktail('http://localhost:8080/api/v1/cocktail/random')
      .subscribe({
        next: (cocktail: Cocktail) => {
          console.log(cocktail);
          this.cocktail = cocktail;
        },
        error: (error) => {
          console.log(error);
        },
      });
  }

  makeGuess() {
    this.cocktailService
      .makeGuess(
        'http://localhost:8080/api/v1/cocktail/guess',
        this.guessRequest
      )
      .subscribe({
        next: (cocktail: Cocktail) => {
          console.log(cocktail);
          this.guessRequest.guess = '';
          this.cocktail = cocktail;
        },
        error: (error) => {
          console.log(error);
        },
      });
  }
}
