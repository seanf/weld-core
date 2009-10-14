/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.weld.bootstrap.events;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.spi.Context;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.ObserverMethod;

import org.jboss.weld.BeanManagerImpl;
import org.jboss.weld.bootstrap.BeanDeployment;
import org.jboss.weld.bootstrap.ExtensionBeanDeployerEnvironment;
import org.jboss.weld.bootstrap.spi.BeanDeploymentArchive;
import org.jboss.weld.bootstrap.spi.Deployment;

public class AfterBeanDiscoveryImpl extends AbstractBeanDiscoveryEvent implements AfterBeanDiscovery
{
   
   public static void fire(BeanManagerImpl deploymentManager, Deployment deployment, Map<BeanDeploymentArchive, BeanDeployment> beanDeployments, ExtensionBeanDeployerEnvironment extensionDeployerEnvironment)
   {
      new AfterBeanDiscoveryImpl(deploymentManager, deployment, beanDeployments, extensionDeployerEnvironment).fire();
   }
   
   protected AfterBeanDiscoveryImpl(BeanManagerImpl deploymentManager, Deployment deployment, Map<BeanDeploymentArchive, BeanDeployment> beanDeployments, ExtensionBeanDeployerEnvironment extensionDeployerEnvironment)
   {
      super(deploymentManager, AfterBeanDiscovery.class, beanDeployments, deployment, extensionDeployerEnvironment);
   }

   public void addDefinitionError(Throwable t)
   {
      getErrors().add(t);
   }
   
   public List<Throwable> getDefinitionErrors()
   {
      return Collections.unmodifiableList(getErrors());
   }
   
   public void addBean(Bean<?> bean)
   {
      BeanManagerImpl beanManager = getOrCreateBeanDeployment(bean.getBeanClass()).getBeanManager();
      beanManager.addBean(bean);
      ProcessBeanImpl.fire(beanManager, bean);
   }

   public void addContext(Context context)
   {
      getBeanManager().addContext(context);
   }

   public void addObserverMethod(ObserverMethod<?, ?> observerMethod)
   {
      getOrCreateBeanDeployment(observerMethod.getBean().getBeanClass()).getBeanManager().addObserver(observerMethod);
   }

}