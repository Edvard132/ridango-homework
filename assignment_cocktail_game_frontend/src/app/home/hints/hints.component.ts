import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Input } from '@angular/core';

@Component({
  selector: 'app-cocktail-hints',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './hints.component.html',
  styleUrl: './hints.component.scss',
})
export class HintsComponent {
  @Input() hints: string[] = [];

  hasHint(index: number): boolean {
    return this.hints && this.hints[index] !== undefined && this.hints[index] !== null;
  }

  getHint(index: number): string | null {
    return this.hasHint(index) ? this.hints[index] : null;
  }
}
