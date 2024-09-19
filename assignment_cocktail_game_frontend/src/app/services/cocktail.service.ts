import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { Cocktail, GuessRequest } from '../../types';

@Injectable({
  providedIn: 'root'
})
export class CocktailService {

  constructor(private apiService: ApiService) { }

  getRandomCocktail = (url: string): Observable<Cocktail> => {
    return this.apiService.get(url);
  }

  makeGuess = (url: string, body: GuessRequest): Observable<Cocktail> => {
    return this.apiService.post(url, body);
  }
}
