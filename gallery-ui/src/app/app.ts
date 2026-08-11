import { Component, OnInit, inject, signal, ChangeDetectionStrategy } from '@angular/core';
import { NgOptimizedImage } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ImageService } from './image.service';
import { Image } from './models/image.model';

@Component({
  selector: 'app-root',
  imports: [NgOptimizedImage, ReactiveFormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: {
    '[class.dark-theme]': 'isNightMode()'
  }
})
export class App implements OnInit {
  private readonly imageService = inject(ImageService);
  private readonly fb = inject(FormBuilder);

  images = signal<Image[]>([]);
  uploading = signal(false);
  isNightMode = signal<boolean>(false);
  previewFlipped = false;
  selectedFile = signal<File | null>(null);
  selectedFilePreview = signal<string | null>(null);
  
  flippedCards = signal<Set<number>>(new Set());

  uploadForm = this.fb.group({
    note: ['', Validators.maxLength(200)],
    theme: ['airmail']
  });

  themes = [
    { id: 'airmail', label: 'Airmail Paper' },
    { id: 'mushroom', label: 'Mushroom Paper' },
    { id: 'star', label: 'Star Paper' },
    { id: 'melody', label: 'Melody Paper' },
    { id: 'nook', label: 'Nook Paper' },
    { id: 'town-hall', label: 'Town-Hall Paper' }
  ];

  ngOnInit() {
    this.loadImages();
  }

  toggleNightMode() {
    this.isNightMode.update(n => !n);
  }

  loadImages() {
    this.imageService.getAllImages().subscribe({
      next: (data) => this.images.set(data),
      error: (err) => console.error('Error fetching images', err)
    });
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      this.selectedFile.set(file);
      const reader = new FileReader();
      reader.onload = e => this.selectedFilePreview.set(e.target?.result as string);
      reader.readAsDataURL(file);
    }
  }

  clearSelection() {
    this.selectedFile.set(null);
    this.selectedFilePreview.set(null);
    this.previewFlipped = false;
    this.uploadForm.reset({ theme: 'airmail', note: '' });
  }

  upload() {
    const file = this.selectedFile();
    if (!file) return;

    this.uploading.set(true);
    const formVal = this.uploadForm.value;
    
    this.imageService.uploadImage(file, formVal.note || '', formVal.theme || 'airmail').subscribe({
      next: (newImage) => {
        this.images.update(prev => [...prev, newImage]);
        this.uploading.set(false);
        this.clearSelection();
        this.previewFlipped = false;
      },
      error: (err) => {
        console.error('Upload failed', err);
        this.uploading.set(false);
      }
    });
  }

  deleteImage(id: number | undefined, event: Event) {
    event.stopPropagation();
    if (id === undefined) return;

    if (confirm('Are you sure you want to delete this mail?')) {
      this.imageService.deleteImage(id).subscribe({
        next: () => {
          this.images.update(prev => prev.filter(img => img.id !== id));
        },
        error: (err) => console.error('Delete failed', err)
      });
    }
  }
  
  toggleCardFlip(id: number | undefined) {
    if (id === undefined) return;
    this.flippedCards.update(set => {
      const newSet = new Set(set);
      if (newSet.has(id)) newSet.delete(id);
      else newSet.add(id);
      return newSet;
    });
  }
  
  isFlipped(id: number | undefined): boolean {
    return id !== undefined && this.flippedCards().has(id);
  }
}
