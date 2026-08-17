import React, { useEffect, useState } from "react";
import ReactDOM from "react-dom/client";
import "./index.css";

const API = "http://localhost:8080";

function App() {
  const [students, setStudents] = useState([]);
  const [name, setName] = useState("");
  const [batch, setBatch] = useState("");
  const [showList, setShowList] = useState(true);
  const [showGrid, setShowGrid] = useState(false);
  const [weeklyData, setWeeklyData] = useState({});

  useEffect(() => { loadStudents(); }, []);

  const loadStudents = () => {
    fetch(`${API}/students`)
      .then(r => r.json())
      .then(setStudents);
  };

  const addStudent = () => {
    if (!name || !batch) return;

    fetch(`${API}/students`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name, batch })
    }).then(() => {
      setName("");
      setBatch("");
      loadStudents();
    });
  };

  const deleteStudent = (id) => {
    fetch(`${API}/students/${id}`, {
      method: "DELETE"
    }).then(() => loadStudents());
  };

  const toggleDay = (studentId, day) => {
    setWeeklyData(prev => {
      const studentWeek = prev[studentId] || {
        Mon:false, Tue:false, Wed:false, Thu:false, Fri:false, Sat:false
      };

      return {
        ...prev,
        [studentId]: {
          ...studentWeek,
          [day]: !studentWeek[day]
        }
      };
    });
  };

  const calculatePercentage = (studentId) => {
    const week = weeklyData[studentId];
    if (!week) return 0;
    const present = Object.values(week).filter(Boolean).length;
    return Math.round((present / 6) * 100);
  };

  const calculateScore = (studentId) => {
    return (calculatePercentage(studentId) / 10).toFixed(1);
  };

  return (
    <div className="container">

      <h1 className="title">ATTENDANCE SYSTEM</h1>

      <div className="card teacher-card">
        <h2>Class Details</h2>
        <p><b>Teacher:</b> Alpesh</p>
        <p><b>Batch:</b> 5</p>
        <p><b>Course:</b> Cloud DevOps</p>
      </div>

      <div className="card">
        <h2>Add Student</h2>
        <input
          placeholder="Student Name"
          value={name}
          onChange={e => setName(e.target.value)}
        />
        <input
          placeholder="Batch"
          value={batch}
          onChange={e => setBatch(e.target.value)}
        />
        <button className="btn primary" onClick={addStudent}>Add</button>
      </div>

      <button className="toggle-btn" onClick={() => setShowList(!showList)}>
        Toggle Attendance Table
      </button>

      <button className="toggle-btn" onClick={() => setShowGrid(!showGrid)}>
        Show Total Students ({students.length})
      </button>

      {showGrid && (
        <div className="card">
          <div className="student-grid">
            {students.map(s => (
              <div className="student-card" key={s.id}>
                <strong>{s.name}</strong>
                <span>{s.batch}</span>
              </div>
            ))}
          </div>
        </div>
      )}

      {showList && (
        <div className="card">
          <table>
            <thead>
              <tr>
                <th>Name</th>
                <th>Batch</th>
                <th>Mon</th>
                <th>Tue</th>
                <th>Wed</th>
                <th>Thu</th>
                <th>Fri</th>
                <th>Sat</th>
                <th>%</th>
                <th>Score</th>
                <th>Delete</th>
              </tr>
            </thead>

            <tbody>
              {students.map(s => {
                const week = weeklyData[s.id] || {};

                return (
                  <tr key={s.id}>
                    <td>{s.name}</td>
                    <td>{s.batch}</td>

                    {["Mon","Tue","Wed","Thu","Fri","Sat"].map(day => (
                      <td key={day}>
                        <button
                          className={`btn ${week[day] ? "present" : "absent"}`}
                          onClick={() => toggleDay(s.id, day)}
                        >
                          {week[day] ? "✔" : "✖"}
                        </button>
                      </td>
                    ))}

                    <td>{calculatePercentage(s.id)}%</td>
                    <td>{calculateScore(s.id)}</td>

                    <td>
                      <button
                        className="btn delete"
                        onClick={() => deleteStudent(s.id)}
                      >
                        🗑
                      </button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      )}

    </div>
  );
}

ReactDOM.createRoot(document.getElementById("root")).render(<App />);
