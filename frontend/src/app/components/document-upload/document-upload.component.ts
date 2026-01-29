import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DocumentService } from '../../services/document.service';

@Component({
  selector: 'app-document-upload',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './document-upload.component.html',
  styleUrl: './document-upload.component.css'
})
export class DocumentUploadComponent {
  @Input() employeeId!: number;
  @Output() uploadComplete = new EventEmitter<void>();

  selectedFile: File | null = null;
  selectedType: string = 'KYC';
  uploading = false;
  message = '';

  constructor(private documentService: DocumentService) { }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  onTypeChange(event: any) {
    this.selectedType = event.target.value;
  }

  upload() {
    if (!this.selectedFile || !this.employeeId) return;

    this.uploading = true;
    this.message = '';

    this.documentService.uploadDocument(this.selectedFile, this.employeeId, this.selectedType)
      .subscribe({
        next: () => {
          this.uploading = false;
          this.message = 'Upload successful!';
          this.selectedFile = null;
          this.uploadComplete.emit();
        },
        error: (err) => {
          this.uploading = false;
          this.message = 'Upload failed.';
          console.error(err);
        }
      });
  }
}
