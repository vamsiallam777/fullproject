import React, { useState } from 'react';
import { Routes, Route } from 'react-router-dom';
import Side from './side';
import Room from './Room';
import './Dashboard.css';

function Dashboard() {
  const [isSidebarOpen, setIsSidebarOpen] = useState(true);

  const DashboardHome = () => (
    <div className="dashboard-home">
      <h1>Welcome to Seating Management System</h1>
      {/* Add your dashboard content here */}
    </div>
  );

  return (
    <div className="dashboard-container">
      <Side onSidebarToggle={setIsSidebarOpen} />
      <main className={`dashboard-content ${isSidebarOpen ? 'sidebar-open' : 'sidebar-closed'}`}>
        <Routes>
          <Route path="/" element={<DashboardHome />} />
          <Route path="/dashboard" element={<DashboardHome />} />
          <Route path="/rooms" element={<Room />} />
        </Routes>
      </main>
    </div>
  );
}

export default Dashboard;