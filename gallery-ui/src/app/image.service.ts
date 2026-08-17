import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Image } from './models/image.model';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class ImageService {
  private readonly http = inject(HttpClient);
  private readonly auth = inject(AuthService);
  private readonly apiUrl = '/api/images';

  getAllImages(): Observable<Image[]> {
    return this.http.get<Image[]>(this.apiUrl, { headers: this.auth.authHeaders() });
  }

  uploadImage(file: File, note: string = '', theme: string = ''): Observable<Image> {
    const formData = new FormData();
    formData.append('file', file);
    if (note) formData.append('note', note);
    if (theme) formData.append('theme', theme);
    return this.http.post<Image>(`${this.apiUrl}/upload`, formData, { headers: this.auth.authHeaders() });
  }

  deleteImage(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: this.auth.authHeaders() });
  }
}