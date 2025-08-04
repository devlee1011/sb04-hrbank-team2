import React, { useEffect } from 'react';
import {Routes, Route, Navigate, HashRouter} from 'react-router-dom';
import { ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import theme from './theme';
import Layout from './components/layout/Layout';
import DashboardPage from './pages/DashboardPage';
import EmployeePage from './pages/EmployeePage';
import DepartmentPage from './pages/DepartmentPage';
import ChangeLogPage from './pages/ChangeLogPage';
import BackupPage from './pages/BackupPage';
import { ErrorProvider, useError } from './contexts/ErrorContext';
import { setGlobalErrorHandler } from './api/client';

// 에러 핸들러 설정을 위한 컴포넌트
const ErrorHandler: React.FC = () => {
  const { showError } = useError();
  
  useEffect(() => {
    // API 클라이언트에 전역 에러 핸들러 설정
    setGlobalErrorHandler(showError);
  }, [showError]);
  
  return null;
};

const App: React.FC = () => {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <ErrorProvider>
        <ErrorHandler />
        <HashRouter>
          <Routes>
            <Route path="/" element={<Navigate to="/dashboard" replace />} />
            <Route
              path="/dashboard"
              element={
                <Layout>
                  <DashboardPage />
                </Layout>
              }
            />
            <Route
              path="/employees"
              element={
                <Layout>
                  <EmployeePage />
                </Layout>
              }
            />
            <Route
              path="/departments"
              element={
                <Layout>
                  <DepartmentPage />
                </Layout>
              }
            />
            <Route
              path="/change-logs"
              element={
                <Layout>
                  <ChangeLogPage />
                </Layout>
              }
            />
            <Route
              path="/backups"
              element={
                <Layout>
                  <BackupPage />
                </Layout>
              }
            />
          </Routes>
        </HashRouter>
      </ErrorProvider>
    </ThemeProvider>
  );
};

export default App;
