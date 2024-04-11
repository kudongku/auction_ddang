import React from 'react';

// 테이블 데이터
const data = [
    { id: 1, age: 30, email: 'john@example.com' },
    { id: 2, age: 25, email: 'jane@example.com' },
    { id: 3, age: 35, email: 'tom@example.com' },
];

const Table = () => {
    return (
        <table className="w-full border-collapse">
            <thead>
            <tr className="border-b">
                <th className="p-3 font-bold text-left">id</th>
                <th className="p-3 font-bold text-left">이름</th>
                <th className="p-3 font-bold text-left">Email</th>
            </tr>
            </thead>
            <tbody>
            {data.map((row, index) => (
                <tr key={index} className={index % 2 === 0 ? "bg-gray-100" : "bg-white"}>
                    <td className="p-3 border">{row.id}</td>
                    <td className="p-3 border">{row.age}</td>
                    <td className="p-3 border">{row.email}</td>
                </tr>
            ))}
            </tbody>
        </table>
    );
};

export default Table;
