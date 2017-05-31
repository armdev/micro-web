package com.project.micro.app;

import com.project.micro.model.Post;
import com.project.micro.resources.JaxRsActivator;
import com.project.micro.resources.MainEndpoint;
import com.project.micro.web.InfoBean;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;

import org.wildfly.swarm.undertow.WARArchive;
import org.wildfly.swarm.Swarm;

public class Main {

    public static void main(String[] args) throws Exception {

        final ClassLoader classLoader = Main.class.getClassLoader();
        Swarm container = new Swarm();     
    

        WARArchive deployment = ShrinkWrap.create(WARArchive.class);
       
        deployment.addClass(MainEndpoint.class);
        deployment.addClass(JaxRsActivator.class);
        deployment.addClass(InfoBean.class);
        deployment.addClass(Post.class);

        deployment.staticContent();

        deployment.addPackage("com.project.micro.app");
        deployment.addPackage("com.project.micro.resources");
        deployment.addPackage("com.project.micro.web");        

        deployment.addAsWebResource(
                new ClassLoaderAsset("webapp/index.xhtml", Main.class.getClassLoader()), "webapp/index.xhtml");
        
        deployment.addAsWebResource(
                new ClassLoaderAsset("webapp/resources", Main.class.getClassLoader()), "webapp/resources");
        
        deployment.addAsWebResource(
                new ClassLoaderAsset("webapp/contracts/default/template.xhtml", Main.class.getClassLoader()), "webapp/contracts/default/template.xhtml");
        
        deployment.addAsWebResource(
                new ClassLoaderAsset("webapp/WEB-INF/includes/main_headers.xhtml", Main.class.getClassLoader()), "webapp/WEB-INF/includes/main_headers.xhtml");
        
        deployment.addAsWebInfResource(
                new ClassLoaderAsset("webapp/WEB-INF/web.xml", Main.class.getClassLoader()), "web.xml");

        deployment.addAsWebInfResource(
                new ClassLoaderAsset("webapp/META-INF/context.xml", Main.class.getClassLoader()), "context.xml");

//        deployment.addAsWebInfResource(
//                new ClassLoaderAsset("webapp/WEB-INF/faces-config.xml", Main.class.getClassLoader()), "faces-config.xml");
        deployment.addAsWebInfResource(
                new ClassLoaderAsset("webapp/WEB-INF/beans.xml", Main.class.getClassLoader()), "beans.xml");

        deployment.addAllDependencies();        

        container.start().deploy(deployment);

    }

}
