package com.jieniuwuliu.jieniu.util;

import com.jieniuwuliu.jieniu.bean.SortModel;

import java.util.Comparator;

public class PinyinComparator implements Comparator<SortModel> {

	public int compare(SortModel o1, SortModel o2) {
		if (o1.getZimu().equals("@")
				|| o2.getZimu().equals("#")) {
			return -1;
		} else if (o1.getZimu().equals("#")
				|| o2.getZimu().equals("@")) {
			return 1;
		} else {
			return o1.getZimu().compareTo(o2.getZimu());
		}
	}

}
