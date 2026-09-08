import React from "react";

const UserPanel=({user,onLogout})=>{
    return(
        <div className="user-panel-container">
            <h3>سلام {user?.name}</h3>
            <button className="logout-btn" onClick={onLogout}>خروج</button>
        </div>
    )
}
export default UserPanel;
