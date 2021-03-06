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

import java.io.IOException;
import java.util.ArrayList;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import com.delphix.delphix.DelphixContainer.ContainerType;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.util.ListBoxModel;

/**
 * Describes a VDB Provision build step for the Delphix plugin
 */
public class ProvisionBuilder extends ContainerBuilder {

    @DataBoundConstructor
    public ProvisionBuilder(String delphixEngine, String delphixGroup, String delphixContainer, String containerName,
            String delphixSnapshot, String delphixCompatibleRepositories, String mountBase) {

        // Hard wire retry count to 0 since we don't want to try to provision multiple times
        super(delphixEngine, delphixGroup, delphixContainer, "0", containerName, delphixSnapshot,
                delphixCompatibleRepositories, mountBase);
    }

    /**
     * Run the provision job
     */
    @Override
    public boolean perform(final AbstractBuild<?, ?> build, Launcher launcher, final BuildListener listener)
            throws IOException, InterruptedException {
        return super.perform(build, listener, DelphixEngine.ContainerOperationType.PROVISIONVDB,
                new ArrayList<HookOperation>(), new ArrayList<HookOperation>());
    }

    @Extension
    public static final class ProvisionDescriptor extends ContainerDescriptor {

        /**
         * Add containers to drop down for Provision action
         */
        public ListBoxModel doFillDelphixEngineItems() {
            return super.doFillDelphixEngineItems();
        }

        /**
         * Add containers to drop down for Provision action
         */
        public ListBoxModel doFillDelphixGroupItems(@QueryParameter String delphixEngine) {
            return super.doFillDelphixGroupItems(delphixEngine);
        }

        /**
         * Add containers to drop down for Provision action
         */
        public ListBoxModel doFillDelphixContainerItems(@QueryParameter String delphixEngine,
                @QueryParameter String delphixGroup) {
            return super.doFillDelphixContainerItems(delphixEngine, delphixGroup, ContainerType.ALL);
        }

        /**
         * Add snapshots to drop down for Provision action
         */
        public ListBoxModel doFillDelphixSnapshotItems(@QueryParameter String delphixEngine,
                @QueryParameter String delphixGroup, @QueryParameter String delphixContainer) {
            return super.doFillDelphixSnapshotItems(delphixEngine, delphixGroup, delphixContainer,
                    DelphixEngine.ContainerOperationType.PROVISIONVDB);
        }

        /**
         * Add snapshots to drop down for Provision action
         */
        public ListBoxModel doFillDelphixCompatibleRepositoriesItems(@QueryParameter String delphixEngine,
                @QueryParameter String delphixGroup, @QueryParameter String delphixContainer,
                @QueryParameter String delphixSnapshot)
                        throws IOException, DelphixEngineException {
            return super.doFillDelphixCompatibleRepositoriesItems(delphixEngine, delphixGroup, delphixContainer,
                    delphixSnapshot);

        }

        /**
         * Name to display for build step
         */
        @Override
        public String getDisplayName() {
            return Messages.getMessage(Messages.PROVISION_OPERATION);
        }
    }
}
