import { Component, OnInit, inject, signal, ChangeDetectionStrategy } from '@angular/core';
import { NgOptimizedImage } from '@angular/common';
import { ImageService } from './image.service';
import { Image } from './models/image.model';

@Component({
  selector: 'app-root',
  imports: [NgOptimizedImage],
  templateUrl: './app.html',
  styleUrl: './app.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class App implements OnInit {
  private readonly imageService = inject(ImageService);

  images = signal<Image[]>([]);
  uploading = signal(false);

  ngOnInit() {
    this.loadImages();
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
      this.uploading.set(true);
      this.imageService.uploadImage(file).subscribe({
        next: (newImage) => {
          this.images.update(prev => [...prev, newImage]);
          this.uploading.set(false);
        },
        error: (err) => {
          console.error('Upload failed', err);
          this.uploading.set(false);
        }
      });
    }
  }

  deleteImage(id: number | undefined) {
    if (id === undefined) return;

    if (confirm('Are you sure you want to delete this image?')) {
      this.imageService.deleteImage(id).subscribe({
        next: () => {
          this.images.update(prev => prev.filter(img => img.id !== id));
        },
        error: (err) => console.error('Delete failed', err)
      });
    }
  }
}
