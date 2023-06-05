import {useState} from "react";

export const useFetching = (callback) => {
    const [isLoading, setLoading] = useState(false);
    const [error, setError] = useState(Error);

    const fetching = async (...args) => {
        try {
            setLoading(true);
            await callback();
        } catch (e) {
            setError(e);
        } finally {
            setLoading(false);
        }
    }

    return [fetching, isLoading, error];
}