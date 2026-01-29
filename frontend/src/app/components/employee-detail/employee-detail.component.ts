import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { EmployeeService, Employee } from '../../services/employee.service';
import { DocumentService, Document } from '../../services/document.service';
import { DocumentUploadComponent } from '../document-upload/document-upload.component';

@Component({
  selector: 'app-employee-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, DocumentUploadComponent],
  templateUrl: './employee-detail.component.html',
  styleUrl: './employee-detail.component.css'
})
export class EmployeeDetailComponent implements OnInit {
  employee: Employee = {
    employeeCode: '', firstName: '', lastName: '', email: '', phone: '',
    department: '', designation: '', dateOfJoining: '', status: 'Active'
  };
  isEditMode = false;
  formEnabled = true;
  documents: Document[] = [];

  constructor(
    private employeeService: EmployeeService,
    private documentService: DocumentService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.formEnabled = false; // Disable form by default for existing employees
      this.loadEmployee(+id);
      this.loadDocuments(+id);
    }
  }

  enableEditing() {
    this.formEnabled = true;
  }

  loadEmployee(id: number) {
    this.employeeService.getEmployee(id).subscribe({
      next: (data) => this.employee = data,
      error: (e) => console.error(e)
    });
  }

  loadDocuments(id: number) {
    this.documentService.getDocuments(id).subscribe({
      next: (data) => this.documents = data,
      error: (e) => console.error(e)
    });
  }

  downloadFile(id: number | undefined, fileName: string) {
    if (!id) return;
    this.documentService.downloadDocument(id).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = fileName;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
      },
      error: (e) => console.error("Download failed", e)
    });
  }

  onSubmit() {
    if (this.isEditMode && this.employee.id) {
      this.employeeService.updateEmployee(this.employee.id, this.employee).subscribe({
        next: () => this.router.navigate(['/employees']),
        error: (e) => console.error(e)
      });
    } else {
      this.employeeService.createEmployee(this.employee).subscribe({
        next: (newEmp) => {
          this.uploadAttachments(newEmp.id!);
        },
        error: (e) => console.error(e)
      });
    }
  }

  offerLetterFile: File | null = null;
  kycFile: File | null = null;

  onOfferFileSelected(event: any) {
    this.offerLetterFile = event.target.files[0];
  }

  onKycFileSelected(event: any) {
    this.kycFile = event.target.files[0];
  }

  uploadAttachments(empId: number) {
    let uploads = [];
    if (this.offerLetterFile) {
      uploads.push(this.documentService.uploadDocument(this.offerLetterFile, empId, 'OFFER_LETTER'));
    }
    if (this.kycFile) {
      uploads.push(this.documentService.uploadDocument(this.kycFile, empId, 'KYC'));
    }

    if (uploads.length > 0) {
      // Ideally use forkJoin, but simple sequential subscription or independent is fine for now
      // Using a simple loop for simplicity
      let completed = 0;
      uploads.forEach(obs => {
        obs.subscribe({
          next: () => {
            completed++;
            if (completed === uploads.length) this.router.navigate(['/employees']);
          },
          error: (e) => {
            console.error("Upload failed", e);
            completed++;
            if (completed === uploads.length) this.router.navigate(['/employees']);
          }
        });
      });
    } else {
      this.router.navigate(['/employees']);
    }
  }

  showResignForm = false;
  resignationReason = '';
  resignationDate = '';
  resignationFile: File | null = null;

  onResignationFileSelected(event: any) {
    this.resignationFile = event.target.files[0];
  }

  submitResignation() {
    if (!this.employee.id) return;

    // Update employee status and details
    const updatedEmployee: Employee = {
      ...this.employee,
      status: 'Resigned',
      resignationReason: this.resignationReason,
      resignationDate: this.resignationDate
    };

    this.employeeService.updateEmployee(this.employee.id, updatedEmployee).subscribe({
      next: () => {
        // Upload letter if exists
        if (this.resignationFile) {
          this.documentService.uploadDocument(this.resignationFile, this.employee.id!, 'RESIGNATION_LETTER').subscribe({
            next: () => {
              alert('Employee resigned successfully');
              this.router.navigate(['/employees']);
            },
            error: (e) => console.error("Error uploading resignation letter", e)
          });
        } else {
          alert('Employee resigned successfully');
          this.router.navigate(['/employees']);
        }
      },
      error: (e) => console.error("Error updating employee", e)
    });
  }
}
