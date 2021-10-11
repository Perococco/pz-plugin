
import jplugman.api.Plugin;
import perobobbot.pzplugin.JPlugin;

module perobobbot.pzplugin {
    requires static lombok;
    requires java.desktop;

    requires jplugman.api;
    requires com.google.common;

    requires org.apache.logging.log4j;

    requires perobobbot.extension;
    requires perobobbot.eventsub;
    requires perobobbot.lang;
    requires perobobbot.oauth;
    requires perobobbot.http;
    requires perobobbot.endpoint;
    requires perobobbot.chat.core;
    requires perobobbot.twitch.client.api;
    requires perobobbot.messaging;
    requires perobobbot.security.reactor;
    requires perobobbot.command;
    requires perobobbot.data.service;
    requires perobobbot.plugin;
    requires perobobbot.access;
    requires spring.webmvc;
    requires java.servlet;
    requires spring.web;
    requires com.fasterxml.jackson.databind;
    requires spring.beans;
    requires spring.context;
    requires reactor.core;
    requires spring.core;
    requires spring.security.core;
    requires org.reactivestreams;

    provides Plugin with JPlugin;

    opens perobobbot.pzplugin.json to com.fasterxml.jackson.databind;

}
