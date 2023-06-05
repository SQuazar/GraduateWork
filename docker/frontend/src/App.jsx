import './App.css'
import {
    createBrowserRouter,
    createRoutesFromElements,
    Navigate,
    Route,
    RouterProvider, Routes,
    useLocation
} from "react-router-dom";
import React, {useEffect, useState} from "react";
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import RolesPage from "./pages/RolesPage";
import AuthService from "./services/auth.service";
import Page from "./components/UI/Page";
import EventBus from "./common/EventBus";
import TelegramUsersPage from "./pages/TelegramUsersPage";
import CategoriesPage from "./pages/CategoriesPage";
import TelegramUserPage from "./pages/TelegramUserPage";
import AnnouncementsHistoryPage from "./pages/AnnouncementsHistoryPage";
import UsersPage from "./pages/UsersPage";
import UserPage from "./pages/UserPage";
import RolePage from "./pages/RolePage";

const App = () => {
    const [user, setUser] = useState(null)

    useEffect(() => {
        const current = AuthService.getCurrentUser();
        let updateAuthoritiesTask;
        if (current) {
            setUser(current)
            updateAuthoritiesTask = setInterval(() => {
                AuthService.updateAuthorities().then(() => {
                    EventBus.dispatch('authoritiesUpdate')
                });
            }, 1000 * 60)
        }
        return () => {
            if (updateAuthoritiesTask)
                clearInterval(updateAuthoritiesTask)
        }
    }, [])

    return (
        <>
            {user ?
                <Page>
                    <Routes>
                        <Route path="/" element={<HomePage/>}/>
                        <Route path="/categories" element={<CategoriesPage/>}/>
                        <Route path="/roles">
                            <Route index element={<RolesPage/>}/>
                            <Route path=":id" element={<RolePage/>}/>
                        </Route>
                        <Route path="/users">
                            <Route index element={<UsersPage/>}/>
                            <Route path=":id" element={<UserPage/>}/>
                        </Route>
                        <Route path="/telegramusers">
                            <Route index element={<TelegramUsersPage/>}/>
                            <Route path=":id" element={<TelegramUserPage/>}/>
                        </Route>
                        <Route path="/announcements" element={<AnnouncementsHistoryPage/>}/>
                    </Routes>
                </Page>
                :
                <Routes>
                    <Route path="*" element={<LoginPage/>}/>
                </Routes>
            }
        </>
    )
}
export default App;