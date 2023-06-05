import React, {useEffect, useState} from 'react';
import {useApi} from "../hooks/useApi";
import {Result, Skeleton} from "antd";
import AnnouncementsList from "../components/AnnouncementsList";

const AnnouncementsHistoryPage = () => {
    const [announcements, setAnnouncements] = useState([{id: 0, text: '', timestamp: '', sender: {id: 0, name: ''}}]);

    const [loadAnnouncements, announcementsLoading, announcementsError] = useApi((data) => {
        setAnnouncements(data.response);
    }, "/announcements?sender=true", "GET")

    useEffect(() => {
        loadAnnouncements();
    }, [])

    if (announcementsError)
        return <Result
            status="403"
            title="403"
            subTitle="У вас нет прав для просмотра этой страницы"
        />
    if (announcementsLoading)
        return <Skeleton/>
    return (
        <AnnouncementsList data={announcements}/>
    );
};

export default AnnouncementsHistoryPage;