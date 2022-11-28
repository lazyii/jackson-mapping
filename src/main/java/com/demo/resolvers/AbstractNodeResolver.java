package com.demo.resolvers;


public abstract class AbstractNodeResolver implements NodeResolver {
    
    protected ResolverManager resolverManager;
    
    public AbstractNodeResolver() {}
    public AbstractNodeResolver(ResolverManager resolverManager) {
        this.resolverManager = resolverManager;
    }
}
