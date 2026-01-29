import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { EmployeeListComponent } from './components/employee-list/employee-list.component';
import { EmployeeDetailComponent } from './components/employee-detail/employee-detail.component';
import { ResignedEmployeesComponent } from './components/resigned-employees/resigned-employees.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    {
        path: '',
        component: DashboardComponent,
        canActivate: [authGuard],
        children: [
            { path: '', redirectTo: 'employees', pathMatch: 'full' },
            { path: 'employees', component: EmployeeListComponent },
            { path: 'employees/new', component: EmployeeDetailComponent },
            { path: 'employees/:id', component: EmployeeDetailComponent },
            { path: 'resigned-employees', component: ResignedEmployeesComponent }
        ]
    },
    { path: '**', redirectTo: '' }
];
