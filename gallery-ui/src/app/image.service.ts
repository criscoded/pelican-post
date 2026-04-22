import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Image } from './models/image.model';

@Injectable({
  providedIn: 'root'
})
export class ImageService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/images';

  getAllImages(): Observable<Image[]> {
    return this.http.get<Image[]>(this.apiUrl);
  }

  uploadImage(file: File): Observable<Image> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<Image>(`${this.apiUrl}/upload`, formData);
  }

  deleteImage(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
