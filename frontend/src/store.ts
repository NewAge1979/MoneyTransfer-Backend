import {applyMiddleware, compose, createStore} from "redux";
import thunk from "redux-thunk";
import {reducers} from "./reducer";

export const store = createStore(
    reducers,
    compose(
        applyMiddleware(thunk),
        //@ts-ignore
        (window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__()) || ((a) => (a))
    )
);