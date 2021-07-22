import React from 'react';

const context = React.createContext();
context.displayName ="mockup";

export const Provider = context.Provider;

export default context; 