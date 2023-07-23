package com.example.postgres.user.routes;

import com.example.postgres.base.routes.BaseRoutes;

public class UserRoutes {
    private final static String ROOT = BaseRoutes.API + "/user";
    public final static String CREATE = ROOT;
    public final static String BY_ID = ROOT + "/{id}";
    public final static String SEARCH = ROOT;
    public final static String TEST = BaseRoutes.NOT_SECURED + "/test";
}
