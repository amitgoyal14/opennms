/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2017-2017 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2017 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.collectd;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.opennms.netmgt.config.prometheus.Group;
import java.util.Objects;

@XmlRootElement(name = "prometheus-collection-request")
@XmlAccessorType(XmlAccessType.NONE)
public class PrometheusCollectionRequestDTO {

    @XmlElement(name="group")
    private List<Group> groups;

    public List<Group> getGroups() {
        if (groups == null) {
            groups = new ArrayList<>();
        }
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof PrometheusCollectionRequestDTO)) {
            return false;
        }
        PrometheusCollectionRequestDTO castOther = (PrometheusCollectionRequestDTO) other;
        return Objects.equals(groups, castOther.groups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groups);
    }

}