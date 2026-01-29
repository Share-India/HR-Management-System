import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Document {
  id?: number;
  fileName: string;
  filePath: string;
  type: string;
  uploadDate: string;
}

@Injectable({
  providedIn: 'root'
})
export class DocumentService {
  private apiUrl = 'http://localhost:8080/api/documents';

  constructor(private http: HttpClient) { }

  uploadDocument(file: File, employeeId: number, type: string): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('employeeId', employeeId.toString());
    formData.append('type', type);

    return this.http.post(`${this.apiUrl}/upload`, formData);
  }

  getDocuments(employeeId: number): Observable<Document[]> {
    return this.http.get<Document[]>(`${this.apiUrl}/employee/${employeeId}`);
  }

  downloadDocument(id: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/download/${id}`, { responseType: 'blob' });
  }
}
