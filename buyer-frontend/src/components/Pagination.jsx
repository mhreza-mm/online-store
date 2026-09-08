import React from "react";

export default function Pagination({ page, totalPages, onPageChange }) {
  return (
    <div className="pagination-container">
      {[...Array(totalPages).keys()].map((i) => (
        <button
          key={i}
          className={`page-btn${page === i + 1 ? " selected" : ""}`}
          onClick={() => onPageChange(i + 1)}
        >
          {i + 1}
        </button>
      ))}
    </div>
  );
}
