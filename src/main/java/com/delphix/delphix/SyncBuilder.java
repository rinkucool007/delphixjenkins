/**
 * Copyright (c) 2015 by Delphix. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.delphix.delphix;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.util.ListBoxModel;
import hudson.model.AbstractBuild;

import java.io.IOException;
import java.util.ArrayList;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import com.delphix.delphix.DelphixContainer.ContainerType;

/**
 * Describes a source Sync build step for the Delphix plugin
 */
public class SyncBuilder extends ContainerBuilder {

    public final ArrayList<HookOperation> preSyncHooks;
    public final ArrayList<HookOperation> postSyncHooks;

    @DataBoundConstructor
    public SyncBuilder(String delphixEngine, String delphixGroup, String delphixContainer, String retryCount,
            ArrayList<HookOperation> preSyncHooks, ArrayList<HookOperation> postSyncHooks) {
        super(delphixEngine, delphixGroup, delphixContainer, retryCount, "", "", "", "");

        // Set the sync hooks to be empty if there is no input
        if (preSyncHooks != null) {
            this.preSyncHooks = preSyncHooks;
        } else {
            this.preSyncHooks = new ArrayList<HookOperation>();
        }
        if (postSyncHooks != null) {
            this.postSyncHooks = postSyncHooks;
        } else {
            this.postSyncHooks = new ArrayList<HookOperation>();
        }
    }

    /**
     * Run the sync job
     */
    @Override
    public boolean perform(final AbstractBuild<?, ?> build, Launcher launcher, final BuildListener listener)
            throws IOException, InterruptedException {
        return super.perform(build, listener, DelphixEngine.ContainerOperationType.SYNC, preSyncHooks, postSyncHooks);
    }

    public ArrayList<HookOperation> getPreSyncHooks() {
        return preSyncHooks;
    }

    public ArrayList<HookOperation> getPostSyncHooks() {
        return postSyncHooks;
    }

    @Extension
    public static final class DescriptorImpl extends ContainerDescriptor {

        /**
         * Add containers to drop down for Refresh action
         */
        public ListBoxModel doFillDelphixEngineItems() {
            return super.doFillDelphixEngineItems();
        }

        /**
         * Add containers to drop down for Refresh action
         */
        public ListBoxModel doFillDelphixGroupItems(@QueryParameter String delphixEngine) {
            return super.doFillDelphixGroupItems(delphixEngine);
        }

        /**
         * Add containers to drop down for Sync action
         */
        public ListBoxModel doFillDelphixContainerItems(@QueryParameter String delphixEngine,
                @QueryParameter String delphixGroup) {
            return super.doFillDelphixContainerItems(delphixEngine, delphixGroup, ContainerType.SOURCE);
        }

        /**
         * Name to display for build step
         */
        @Override
        public String getDisplayName() {
            return Messages.getMessage(Messages.SYNC_OPERATION);
        }
    }
}
