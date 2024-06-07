package com.crawler.challenge.mocks;

import org.w3c.dom.NodeList;

public class NodeListMock implements NodeList {
    @Override
    public org.w3c.dom.Node item(int index) {
        return new ElementMock();
    }

    @Override
    public int getLength() {
        return 2;
    }
}