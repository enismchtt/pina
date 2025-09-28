import React from 'react';
import './MealComments.css';

const MealComments = ({ mealId, comments }) => {
  return (
    <div className="meal-comments">
      <h3>Comments</h3>
      <div className="comments-list">
        {comments?.map((comment, index) => (
          <div key={index} className="comment">
            <div className="comment-header">
              <span className="user">User {comment.user}</span>
              <span className="rating">⭐ {comment.rating}/5</span>
            </div>
            <p className="comment-text">{comment.text}</p>
            <button className="reply-button">Reply</button>
          </div>
        ))}
        {(!comments || comments.length === 0) && (
          <p className="no-comments">No comments yet.</p>
        )}
      </div>
    </div>
  );
};

export default MealComments; 