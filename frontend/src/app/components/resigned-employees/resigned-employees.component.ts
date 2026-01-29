import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { EmployeeService, Employee } from '../../services/employee.service';

@Component({
    selector: 'app-resigned-employees',
    standalone: true,
    imports: [CommonModule, RouterModule],
    templateUrl: './resigned-employees.component.html',
    styleUrl: './resigned-employees.component.css'
})
export class ResignedEmployeesComponent implements OnInit {
    employees: Employee[] = [];

    constructor(
        private employeeService: EmployeeService,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.loadEmployees();
    }

    loadEmployees() {
        this.employeeService.getResignedEmployees().subscribe({
            next: (data) => {
                console.log('Resigned Employees Data:', data);
                this.employees = data || [];
            },
            error: (e) => console.error(e)
        });
    }

    navigateToDetail(id: number | undefined) {
        if (id) {
            this.router.navigate(['/employees', id]);
        }
    }

    deleteEmployee(event: Event, id: number | undefined) {
        event.stopPropagation(); // Prevent row click
        if (id && confirm('Are you sure you want to delete this employee?')) {
            this.employeeService.deleteEmployee(id).subscribe({
                next: () => {
                    this.loadEmployees();
                },
                error: (e) => console.error(e)
            });
        }
    }
}
