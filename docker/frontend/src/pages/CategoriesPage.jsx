import React, {useEffect, useState} from 'react';
import {useApi} from "../hooks/useApi";
import {message, Result, Skeleton} from "antd";
import CategoriesList from "../components/CategoriesList";
import makeRequest from "../api/API";

const CategoriesPage = () => {
    const [categories, setCategories] = useState([]);

    const [loadCategories, isLoading, error] = useApi((data) => {
        setCategories(data.response)
    }, '/categories', 'GET')

    useEffect(() => {
        loadCategories();
    }, [])

    const handleDeleteCategory = (id) => {
        makeRequest(`/categories/${id}`, 'DELETE').then((data) => {
            if (data.code === 200) {
                message.success('Категория удалена').then()
                loadCategories();
            }
        })
    }

    const createCategory = (category) => {
        makeRequest(`/categories?name=${category.name}`, 'PUT').then((data) => {
            if (data.code === 200) {
                message.success('Категория добавлена').then();
                loadCategories();
            }
        })
    }

    if (isLoading)
        return <Skeleton/>
    if (error)
        return <Result
            status="403"
            title="403"
            subTitle="У вас нет прав для просмотра этой страницы"
        />
    return (
        <CategoriesList data={categories} onDelete={handleDeleteCategory} onCreate={createCategory}/>
    );
};

export default CategoriesPage;