/*
 * DocDoku, Professional Open Source
 * Copyright 2006 - 2013 DocDoku SARL
 *
 * This file is part of DocDokuPLM.
 *
 * DocDokuPLM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DocDokuPLM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with DocDokuPLM.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.docdoku.client.data;

import com.docdoku.core.document.Folder;
import com.docdoku.core.document.DocumentMaster;

public class FolderTreeNode implements Comparable<FolderTreeNode> {

    protected Folder mFolder;

    protected FolderTreeNode mParent;

    public FolderTreeNode(String pCompletePath, FolderTreeNode pParent) {
        this(new Folder(pCompletePath), pParent);
    }

    public FolderTreeNode(Folder pFolder, FolderTreeNode pParent) {
        mFolder = pFolder;
        mParent = pParent;
    }

    public FolderTreeNode getFolderChild(int pIndex) {
        FolderTreeNode[] folderTreeNodes = MainModel.getInstance()
                .getFolderTreeNodes(this);
        return folderTreeNodes[pIndex];
    }

    public Object getElementChild(int pIndex) {
        DocumentMaster[] docMs = MainModel.getInstance().findDocMsByFolder(mFolder.getCompletePath());
        if(pIndex<docMs.length)
            return docMs[pIndex];
        else 
            return null;
    }

    public int getFolderIndexOfChild(Object pChild) {
        for (int i = 0; i < folderSize(); i++)
            if (getFolderChild(i).equals(pChild))
                return i;
        return -1;
    }

    public int folderSize() {
        FolderTreeNode[] folderTreeNodes = MainModel.getInstance()
                .getFolderTreeNodes(this);
        return folderTreeNodes.length;
    }

    public int elementSize() {
        DocumentMaster[] docMs = MainModel.getInstance().findDocMsByFolder(
                mFolder.getCompletePath());
        return docMs.length;
    }

    public String getPosition() {
        return mParent.getPosition() + "."
                + (mParent.getFolderIndexOfChild(this) + 1);
    }

    @Override
    public String toString() {
        boolean numbered = MainModel.getInstance().getElementsTreeModel()
                .getNumbered();
        if (numbered)
            return getPosition() + "  " + mFolder.getShortName();
        else
            return mFolder.getShortName();
    }

    public String getName(){
        return mFolder.getShortName();
    }
    
    public String getCompletePath() {
        return mFolder.getCompletePath();
    }
    
    public Folder getFolder() {
        return mFolder;
    }

    @Override
    public boolean equals(Object pObj) {
        if (!(pObj instanceof FolderTreeNode))
            return false;
        FolderTreeNode treeNode = (FolderTreeNode) pObj;
        return treeNode.mFolder.equals(mFolder);
    }

    @Override
    public int hashCode() {
        return mFolder.hashCode();
    }

    @Override
    public int compareTo(FolderTreeNode pFolderTreeNode) {
        return mFolder.compareTo(pFolderTreeNode.mFolder);
    }
}