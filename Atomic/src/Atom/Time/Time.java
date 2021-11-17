/*
 * Copyright 2021 Itzbenz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package Atom.Time;

import Atom.Struct.PoolObject;
import Atom.Utility.Utility;

import java.util.concurrent.TimeUnit;

public class Time implements PoolObject.Object, Cloneable {
    public TimeUnit tu;
    public long src;
    
    public Time() {
        this(TimeUnit.NANOSECONDS, System.nanoTime());
    }
    
    public Time(TimeUnit tu) {
        this(tu, tu.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS));
    }
    
    public Time(TimeUnit tu, long src) {
        this.tu = tu;
        this.src = src;
    }
    
    //convert to another time unit with its source, note affect current object
    public Time convert(TimeUnit to) {
        if (tu == to) return this;//bruh
        src = to.convert(src, tu);
        tu = to;
        return this;
    }
    
    public long convert0(TimeUnit to) {
        return to.convert(src, tu);
    }
    
    
    //get elapsed time based on current time
    public Time elapsed() {
        return elapsed(tu.equals(TimeUnit.NANOSECONDS) ? new Time() : new Time(TimeUnit.MILLISECONDS));
    }
    
    public Time elapsed(Time time) {
        return new Time(tu, elapsed0(time));
    }
    
    public long elapsed0(Time time) {
        return elapsed0(tu.convert(time.src, time.tu));
    }
    
    public long elapsed0(long tg) {
        return tg - src;
    }
    
    public String elapsedS(Time time) {
        return elapsed(time).toString();
    }
    
    public String elapsedS() {
        return elapsed().toString();
    }
    
    
    @Override
    public String toString() {
        return src + " " + Utility.capitalizeEnforce(tu.toString());
    }
    
    @Override
    public void reset() {
        src = 0;
        tu = TimeUnit.NANOSECONDS;
    }
    
    @Override
    public Time clone() {
        try {
            return (Time) super.clone();
        }catch(CloneNotSupportedException e){
            throw new AssertionError();
        }
    }
}
